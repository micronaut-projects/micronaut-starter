$version = '2.3.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = 'D2F1748C830181219806EDDB6FF7E7636126C1D4F19DD8A5A946096DB972E673'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
