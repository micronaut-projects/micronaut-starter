$version = '4.3.2'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '1B0E5D4101B665583A26D29C88C14566EA4FC7B6646AD3162C9CCA9D2B17F051'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
