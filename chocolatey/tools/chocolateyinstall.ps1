$version = '3.9.5'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '229F9EF0441BAFB98E854F11F2CB0E812417A47EBB3425156DDE2EDA3B96697F'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
