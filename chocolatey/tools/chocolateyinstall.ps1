$version = '3.2.4'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'D3853F1FAC24A2536880C6BB3514195CAC88AADDC59BD1A070249F9F2260F415'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
