$version = '2.5.3'

$packageArgs = @{
  packageName   = $env:ChocolateyPackageName
  unzipLocation = "$(Split-Path -parent $MyInvocation.MyCommand.Definition)"
  url           = "https://github.com/micronaut-projects/micronaut-starter/releases/download/v$version/mn-win-amd64-v$version.zip"
  checksum      = '951F681AC7B191B5E05C9D345184AA482424E11E8FE17A322B97E39AE8084519'
  checksumType  = 'sha256'
}

Install-ChocolateyZipPackage @packageArgs
