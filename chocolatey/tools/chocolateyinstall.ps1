$version = '3.5.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'C3A3E755424E0EA7977EA172CA3368385D0FC6B40024EDE2A2A6900BDD0382F3'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
